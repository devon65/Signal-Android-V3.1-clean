// Code generated by dagger-compiler.  Do not edit.
package org.thoughtcrime.securesms;

import dagger.MembersInjector;
import dagger.internal.Binding;
import dagger.internal.Linker;
import java.util.Set;
import javax.inject.Provider;

/**
 * A {@code Binding<CreateProfileActivity>} implementation which satisfies
 * Dagger's infrastructure requirements including:
 *
 * Owning the dependency links between {@code CreateProfileActivity} and its
 * dependencies.
 *
 * Being a {@code Provider<CreateProfileActivity>} and handling creation and
 * preparation of object instances.
 *
 * Being a {@code MembersInjector<CreateProfileActivity>} and handling injection
 * of annotated fields.
 */
public final class CreateProfileActivity$$InjectAdapter extends Binding<CreateProfileActivity>
    implements Provider<CreateProfileActivity>, MembersInjector<CreateProfileActivity> {
  private Binding<org.whispersystems.signalservice.api.SignalServiceAccountManager> accountManager;
  private Binding<BaseActionBarActivity> supertype;

  public CreateProfileActivity$$InjectAdapter() {
    super("org.thoughtcrime.securesms.CreateProfileActivity", "members/org.thoughtcrime.securesms.CreateProfileActivity", NOT_SINGLETON, CreateProfileActivity.class);
  }

  /**
   * Used internally to link bindings/providers together at run time
   * according to their dependency graph.
   */
  @Override
  @SuppressWarnings("unchecked")
  public void attach(Linker linker) {
    accountManager = (Binding<org.whispersystems.signalservice.api.SignalServiceAccountManager>) linker.requestBinding("org.whispersystems.signalservice.api.SignalServiceAccountManager", CreateProfileActivity.class, getClass().getClassLoader());
    supertype = (Binding<BaseActionBarActivity>) linker.requestBinding("members/org.thoughtcrime.securesms.BaseActionBarActivity", CreateProfileActivity.class, getClass().getClassLoader(), false, true);
  }

  /**
   * Used internally obtain dependency information, such as for cyclical
   * graph detection.
   */
  @Override
  public void getDependencies(Set<Binding<?>> getBindings, Set<Binding<?>> injectMembersBindings) {
    injectMembersBindings.add(accountManager);
    injectMembersBindings.add(supertype);
  }

  /**
   * Returns the fully provisioned instance satisfying the contract for
   * {@code Provider<CreateProfileActivity>}.
   */
  @Override
  public CreateProfileActivity get() {
    CreateProfileActivity result = new CreateProfileActivity();
    injectMembers(result);
    return result;
  }

  /**
   * Injects any {@code @Inject} annotated fields in the given instance,
   * satisfying the contract for {@code Provider<CreateProfileActivity>}.
   */
  @Override
  public void injectMembers(CreateProfileActivity object) {
    object.accountManager = accountManager.get();
    supertype.injectMembers(object);
  }

}
