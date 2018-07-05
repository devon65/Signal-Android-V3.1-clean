// Code generated by dagger-compiler.  Do not edit.
package org.thoughtcrime.securesms.jobs;

import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;

/**
 * A {@code Binding<MultiDeviceProfileKeyUpdateJob>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 *
 * Owning the dependency links between {@code MultiDeviceProfileKeyUpdateJob} and its
 * dependencies.
 *
 * Being a {@code Provider<MultiDeviceProfileKeyUpdateJob>} and handling creation and
 * preparation of object instances.
 *
 * Being a {@code MembersInjector<MultiDeviceProfileKeyUpdateJob>} and handling injection
 * of annotated fields.
 */
public final class MultiDeviceProfileKeyUpdateJob$$InjectAdapter extends Binding<MultiDeviceProfileKeyUpdateJob>
    implements MembersInjector<MultiDeviceProfileKeyUpdateJob> {
  private Binding<org.whispersystems.signalservice.api.SignalServiceMessageSender> messageSender;
  private Binding<MasterSecretJob> supertype;

  public MultiDeviceProfileKeyUpdateJob$$InjectAdapter() {
    super(null, "members/org.thoughtcrime.securesms.jobs.MultiDeviceProfileKeyUpdateJob", NOT_SINGLETON, MultiDeviceProfileKeyUpdateJob.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    messageSender = (Binding<org.whispersystems.signalservice.api.SignalServiceMessageSender>) linker.requestBinding("org.whispersystems.signalservice.api.SignalServiceMessageSender", MultiDeviceProfileKeyUpdateJob.class, getClass().getClassLoader());
    supertype = (Binding<MasterSecretJob>) linker.requestBinding("members/org.thoughtcrime.securesms.jobs.MasterSecretJob", MultiDeviceProfileKeyUpdateJob.class, getClass().getClassLoader(), false, true);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(messageSender);
    injectMembersBindings.add(supertype);
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<MultiDeviceProfileKeyUpdateJob>}.
   */
  @Override
  public void injectMembers(MultiDeviceProfileKeyUpdateJob object) {
    object.messageSender = messageSender.get();
    supertype.injectMembers(object);
  }

}
